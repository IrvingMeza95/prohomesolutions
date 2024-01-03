package com.EF2.prohomesolutions.services;

import com.EF2.prohomesolutions.enums.JobStatus;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Job;
import com.EF2.prohomesolutions.models.Message;
import com.EF2.prohomesolutions.models.Provider;
import com.EF2.prohomesolutions.repositories.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepo jobRepo;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private NotificationService notificationService;

    public void create(Customer customer, Provider provider, String message) {
        Job job = Job.builder()
                .cust(customer)
                .prov(provider)
                .jobStatus(JobStatus.PENDING)
                .date(new Date())
                .totalPrice((double) 0)
                .build();
        job = jobRepo.save(job);
        String senderId = customer.getId();
        String receiverId = provider.getId();
        Message message1 = messageService.save(job, senderId,receiverId,message);
        String notiTitle = "New Order";
        String notiDetails = "New order from customer " + customer.getName() + ".";
        String url = "/job/chat-messages/" + job.getId();
        notificationService.create(provider.getId(), provider.getUserType().name(), notiTitle, notiDetails, url, customer.getId());
    }

    public List<Job> orderByDateDes(List<Job> jobs){
        return jobs.stream().sorted(Comparator.comparing(Job::getDate).reversed())
                .collect(Collectors.toList());
    }

    public Job changeStatus(String id, String jobStatus) throws MyException {
        Job job = jobRepo.findById(id).get();
        if (job.getJobStatus().ordinal() > JobStatus.valueOf(jobStatus).ordinal()){
            throw new MyException("Is not posible to set the selected status.");
        }
        job.setJobStatus(JobStatus.valueOf(jobStatus));
        jobRepo.save(job);
        return job;
    }

    public void delete(String id) throws MyException {
        Job job = jobRepo.findById(id).get();
        if (job.getJobStatus().ordinal() > 0){
            throw new MyException("Action cannot be completed.");
        }
        messageService.deleteAllMessagesFromOrder(job);
        jobRepo.delete(job);
    }

    public Job findById(String id) {
        return jobRepo.findById(id).get();
    }

    public void chatSendMessage(Job job, HttpSession session, String message) {
        String userType = (String) session.getAttribute("userType");
        String notiTitle = "New Message";
        String notiDetails = "";
        String url = "/job/chat-messages/" + job.getId();
        if (userType.equals("CUSTOMER")){
            messageService.save(job, job.getCust().getId(), job.getProv().getId(), message);
            notiDetails = "Customer " + job.getCust().getName() + " send you a new message.";
            notificationService.create(job.getProv().getId(), job.getProv().getUserType().name(),
                    notiTitle, notiDetails, url, job.getCust().getId());
        }
        if (userType.equals("PROVIDER")){
            messageService.save(job, job.getProv().getId(), job.getCust().getId(), message);
            notiDetails = "Provider " + job.getProv().getName() + " send you a new message.";
            notificationService.create(job.getCust().getId(), job.getCust().getUserType().name(),
                    notiTitle, notiDetails, url, job.getProv().getId());
        }
    }

    public List<Message> getChatMessages(Job job){
        return messageService.orderByDateDesc(job.getMessages());
    }

    public void saveChanges(String id, String jobStatus, String price) throws MyException {
        Job job = changeStatus(id, jobStatus);
        job.setTotalPrice(Double.valueOf(price));
        jobRepo.save(job);
    }


}
