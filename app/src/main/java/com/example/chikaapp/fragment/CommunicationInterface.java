package com.example.chikaapp.fragment;

import com.example.chikaapp.model.Image;
import com.example.chikaapp.model.Scripts;

import java.util.ArrayList;

public interface CommunicationInterface {
    void RoomToDevices(String idRoom, String roomName);
    void DeviceToProducts(String idRoom);
    void ProductsToButtonNotUsed(ArrayList arrayList, int maxButton, String idRoom, String topic, String type);
    void ButtonNotUsedToAddDevice(int button, String idRoom, String topic, String type);
    void ProductsToAddIRDevice(String idRoom, String topic, String type);
    void ScriptToDetailScripts(Scripts scripts);
    void ScriptToAddScript(Image img);
    void DialogImageToAddScript(Image image);
}
