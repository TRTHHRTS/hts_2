package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static javax.swing.JOptionPane.showMessageDialog;

public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer());
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        public void initChannel(SocketChannel ch) {
            ChannelPipeline p = ch.pipeline();
            p.addLast("decoder", new HttpRequestDecoder());
            p.addLast("encoder", new HttpResponseEncoder());
            p.addLast("handler", new ServerHandler());
        }
    }

    class ServerHandler extends SimpleChannelInboundHandler<Object> {

        private HttpRequest request;
        private final StringBuilder buf = new StringBuilder();
        private Map<String, UriHandlerBased> handlers = new HashMap<>();

        ServerHandler() {
            if (handlers.size()==0) {
                try {
                    for (Class c : ReflectionTools.getClasses(getClass().getPackage().getName() + ".handlers")) {
                        Annotation annotation = c.getAnnotation(Mapped.class);
                        if (annotation!=null) {
                            handlers.put(((Mapped) annotation).uri(), (UriHandlerBased)c.newInstance());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
            UriHandlerBased handler = null;
            if (msg instanceof HttpRequest) {
                HttpRequest request = this.request = (HttpRequest) msg;
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
                buf.setLength(0);
                String context = queryStringDecoder.path();
                handler = handlers.get(context);
                if (handler!=null) {
                    try {
                        handler.process(request, buf);
                    } catch (Exception e) {
                        showMessageDialog(null, e.getLocalizedMessage());
                    }
                }
            }
            if (msg instanceof LastHttpContent) {
                FullHttpResponse response = new DefaultFullHttpResponse (
                        HTTP_1_1,
                        ((LastHttpContent) msg).getDecoderResult().isSuccess()? OK : BAD_REQUEST,
                        Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8)
                );
                response.headers().set(CONTENT_TYPE, handler != null ? handler.getContentType() : "text/plain; charset=UTF-8");

                if (isKeepAlive(request)) {
                    response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                    response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                }
                ctx.write(response);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}