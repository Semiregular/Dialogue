package cn.org.dialogue.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
@Slf4j
@ServerEndpoint("/ws/{idxUser}")
@Component
public class WebSocketServer {
    /**
     * 统计在线人数
     */
    private static int onlineCount = 0;
    /**
     * 存放所有 httpsession 连接
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
    /**
     * httpsession 需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 用户 id
     */
    private int idxUser;

    @OnOpen
    public void onOpen(Session session, @PathParam("idxUser") int idxUser){
        this.session = session;
        this.idxUser = idxUser;

        webSocketSet.add(this);
        addOnlineCount();

//        try {
//            sendMessage("连接成功");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        log.debug(" 有一连接关闭,窗口为：" + idxUser + " 当前在线人数为 " + getOnlineCount());
    }

    /*
    @OnMessage
    public void onMessage(String content, Session session) {
        log.debug(" 收到来自窗口 " + idxUser + " 的信息 :" + content);
        String returnMessage = "你刚才说：" + content;
        try {
            session.getBasicRemote().sendText(returnMessage);
        }
        catch (IOException e) {
            System.out.println("返回数据失败");
        }
    }
    */

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 推送远程消息
     * @param message 消息
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    public static int sendByIdxUser(Integer idxFrom, Integer idxTo) throws IOException {
        log.info(" 推送消息到窗口 " + idxTo + " ，推送内容 :" + idxFrom);
        for (WebSocketServer item : webSocketSet) {
            try {
                if (item.idxUser == idxTo) {
                    String message = idxFrom.toString();
                    item.sendMessage(message);
                    return 1;
                }
            } catch (IOException e) {
                continue;
            }
        }
        return 0;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}