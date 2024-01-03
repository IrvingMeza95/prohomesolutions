package com.EF2.prohomesolutions.services;

import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Job;
import com.EF2.prohomesolutions.models.Message;
import com.EF2.prohomesolutions.models.Provider;
import com.EF2.prohomesolutions.repositories.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public Message save(Job job, String senderId, String receiverId, String message){
        Message message1 = Message.builder()
                .job(job)
                .senderId(senderId)
                .receiverId(receiverId)
                .date(new Date())
                .message(message)
                .build();
        return messageRepo.save(message1);
    }

    public void deleteAllMessagesFromOrder(Job job) {
        messageRepo.deleteAll(job.getMessages());
    }

    public List<Message> orderByDateDesc(List<Message> messages){
        return messages.stream().sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());
    }

}
