package com.structure.ChatVedioConsultation;


import com.structure.DataBase.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {



    public static  void addMessage(ChatClient chatClient){



        String query ="INSERT INTO chats(sender , receiver , message ) VALUES(?,?,?)";
        try(Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){


            preparedStatement.setString(1,chatClient.getSender());
            preparedStatement.setString(2,chatClient.getReceiver());
            preparedStatement.setString(3,chatClient.getMessage());
            int rows = preparedStatement.executeUpdate();
            if(rows>0){
                System.out.println("message added successfully");
            }


        }
        catch (Exception e){
e.printStackTrace();
        }
    }
    public static  List<ChatClient> getMessages(String sender,String receiver) throws SQLException {

        List<ChatClient> chat = new ArrayList<>();
        String query ="Select * FROM chats WHERE sender = ? and receiver = ? ORDER BY timestamp DESC";
        try(Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1,sender);
            preparedStatement.setString(2,receiver);

            ResultSet resultSet = preparedStatement.executeQuery();
            LocalDateTime localDateTime = null;
            while (resultSet.next()){
                resultSet.getString("sender");
                resultSet.getString("receiver");
                resultSet.getString("message");
                resultSet.getTimestamp("timestamp");
                 localDateTime = resultSet.getTimestamp("timestamp").toLocalDateTime();
                ChatClient chatClient = new ChatClient(resultSet.getString("sender"),resultSet.getString("receiver"),resultSet.getString("message"),localDateTime);
                chat.add(chatClient);
            }
            System.out.println("the size of chat list is " + chat.size());

        }
        return chat;
    }

}
