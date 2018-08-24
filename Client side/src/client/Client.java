package com.example.zvi.basicApp.client;

import com.example.zvi.basicApp.dataTransfer.DataWriter;
import com.example.zvi.basicApp.dataTransfer.DataReader;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Client {

    private Socket clientSocket;
    private DataReader reader;
    private DataWriter writer;

    public Client() {
        try {
            clientSocket = new Socket("10.0.2.2", 8125);
            reader = new DataReader(clientSocket.getInputStream());
            writer = new DataWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
        }
    }

    public String[][] sendSelectCommand(Map<Integer,InfoType> args){
        writer.write(CommandEnum.sql.ordinal());
        //send query
        writer.write(args.get(0).getText());
        InfoType arg;
        //send arguments
        for(int i=1 ; i<args.size();i++){
            arg = args.get(i);
            if(arg==null)
                continue;
            if(arg.isNumber())
                writer.write(arg.getNumber());
            else
                writer.write(arg.getText());
        }
        //read confirmation
        reader.readInt();

        //read resultset sizes
        int rows = reader.readInt();
        int cols = reader.readInt();

        String[][] rs = new String[rows+1][cols];

        for (int i=0 ; i<rows+1;i++){
            for (int j=0 ; j<cols;j++){
                    rs[i][j] = reader.readString();
            }
        }
        return rs;
    }

    public int sendCommand(CommandEnum command,Map<Integer,InfoType> args){
            writer.write(command.ordinal());
            InfoType arg;
            //send arguments
            for(int i=0 ; i<args.size();i++){
                arg = args.get(i);
                if(arg==null)
                    continue;
                if(arg.isNumber())
                    writer.write(arg.getNumber());
                else
                    writer.write(arg.getText());
            }
            //read confirmation
            return reader.readInt();
    }
}
