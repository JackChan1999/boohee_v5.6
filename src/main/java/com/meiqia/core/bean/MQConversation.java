package com.meiqia.core.bean;

import java.util.ArrayList;
import java.util.List;

public class MQConversation {
    private String agent_id;
    private int    agent_msg_num;
    private int    assignee;
    private String client_first_send_time;
    private int    client_msg_num;
    private int    converse_duration;
    public  long   created_on;
    private String ended_by;
    public  String ended_on;
    private long   enterprise_id;
    private int    first_response_wait_time;
    private long   id;
    private String last_msg_content;
    public  long   last_msg_created_on;
    private long   last_updated;
    private List<MQMessage> messageList = new ArrayList();
    private int    msg_num;
    private String track_id;
    private String visit_id;

    public boolean equals(Object obj) {
        MQConversation mQConversation = (MQConversation) obj;
        return this.track_id.equals(mQConversation.getTrack_id()) && this.id == mQConversation
                .getId();
    }

    public String getAgent_id() {
        return this.agent_id;
    }

    public int getAgent_msg_num() {
        return this.agent_msg_num;
    }

    public int getAssignee() {
        return this.assignee;
    }

    public String getClient_first_send_time() {
        return this.client_first_send_time;
    }

    public int getClient_msg_num() {
        return this.client_msg_num;
    }

    public int getConverse_duration() {
        return this.converse_duration;
    }

    public String getEnded_by() {
        return this.ended_by;
    }

    public long getEnterprise_id() {
        return this.enterprise_id;
    }

    public int getFirst_response_wait_time() {
        return this.first_response_wait_time;
    }

    public long getId() {
        return this.id;
    }

    public String getLast_msg_content() {
        return this.last_msg_content;
    }

    public long getLast_updated() {
        return this.last_updated;
    }

    public List<MQMessage> getMessageList() {
        return this.messageList;
    }

    public int getMsg_num() {
        return this.msg_num;
    }

    public String getTrack_id() {
        return this.track_id;
    }

    public String getVisit_id() {
        return this.visit_id;
    }

    public void setAgent_id(String str) {
        this.agent_id = str;
    }

    public void setAgent_msg_num(int i) {
        this.agent_msg_num = i;
    }

    public void setAssignee(int i) {
        this.assignee = i;
    }

    public void setClient_first_send_time(String str) {
        this.client_first_send_time = str;
    }

    public void setClient_msg_num(int i) {
        this.client_msg_num = i;
    }

    public void setConverse_duration(int i) {
        this.converse_duration = i;
    }

    public void setCreated_on(long j) {
        this.created_on = j;
    }

    public void setEnded_by(String str) {
        this.ended_by = str;
    }

    public void setEnded_on(String str) {
        this.ended_on = str;
    }

    public void setEnterprise_id(long j) {
        this.enterprise_id = j;
    }

    public void setFirst_response_wait_time(int i) {
        this.first_response_wait_time = i;
    }

    public void setId(long j) {
        this.id = j;
    }

    public void setLast_msg_content(String str) {
        this.last_msg_content = str;
    }

    public void setLast_msg_created_on(long j) {
        this.last_msg_created_on = j;
    }

    public void setLast_updated(long j) {
        this.last_updated = j;
    }

    public void setMessageList(List<MQMessage> list) {
        this.messageList = list;
    }

    public void setMsg_num(int i) {
        this.msg_num = i;
    }

    public void setTrack_id(String str) {
        this.track_id = str;
    }

    public void setVisit_id(String str) {
        this.visit_id = str;
    }
}
