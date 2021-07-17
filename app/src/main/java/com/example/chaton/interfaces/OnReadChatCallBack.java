package com.example.chaton.interfaces;

import com.example.chaton.model.chat.Chats;

import java.util.List;

public interface OnReadChatCallBack {
    void onReadSuccess(List<Chats> list);
    void onReadFailed();
}
