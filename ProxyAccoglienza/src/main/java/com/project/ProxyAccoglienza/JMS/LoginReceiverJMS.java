package com.project.ProxyAccoglienza.JMS;

import com.project.ProxyAccoglienza.web.LoginResponse;
import com.project.ProxyAccoglienza.web.Post;
import com.project.ProxyAccoglienza.web.Webhook;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@Service
public class LoginReceiverJMS implements MessageListener {

    private final Post poster = new Post();

    @JmsListener(destination = "CodaLoginAccoglienza")
    @Override
    public void onMessage(@NotNull Message message) {

        LoginResponse msg_received = new LoginResponse();
        try {
            msg_received = (LoginResponse) message.getBody(LoginResponse.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
        Webhook.Acceptance.put(msg_received.user, msg_received.url);

        poster.createPost("http://"+ msg_received.url+"/login","Login successful");
        //in realtà nel body andrebbe inviato l'indirizzo del proxy in questione
    }
}