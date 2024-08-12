package com.summary;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.client.ImClient;
import com.summary.im.client.handler.MsgSendHandler;
import com.summary.im.enums.BodyType;
import com.summary.im.enums.ClientType;
import com.summary.im.enums.MsgType;

import java.util.Scanner;

/**
 * @author jie.luo
 * @since 2024/8/9
 */
public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        ImClient imClient = ImClient.instance("192.168.31.7", 9999, 2L, ClientType.window);

        int i = 0;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            i++;
            String line = scanner.nextLine();
            if (line.equals("logout")) {
                imClient.logout();
                Thread.sleep(20000);
                break;
            } else {
                MsgSendHandler.sendMsg(ImMsgRequest.builder()
                        .chatId(1822906204242837505L)
                        .msgType(MsgType.chat.getCode())
                        .toUserId(1L)
                        .msgIdClient("" + i)
                        .bodyType(BodyType.text.getCode())
                        .body(line)
                        .build());
            }
        }
        scanner.close();
    }
}
