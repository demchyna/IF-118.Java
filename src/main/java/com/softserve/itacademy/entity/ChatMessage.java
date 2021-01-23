package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ChatMessage extends BasicEntity {

    @ManyToOne
    @JoinColumn(name = "chat_room_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_message_chat_room"))
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_user_message"))
    private User user;

    @Size(min = 1, max=255)
    private String content;

    private MessageStatus status;

    public enum MessageStatus {
        RECEIVED, DELIVERED
    }
}
