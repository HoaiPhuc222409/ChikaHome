package com.example.chikaapp.fragment;

import java.util.ArrayList;

public interface CommunicationInterface {
    void RoomToDevices(String idRoom, String roomName);
    void DeviceToProducts(String idRoom);
    void ProductsToButtonNotUsed(ArrayList arrayList, int maxButton, String idRoom, String topic, String type);
    void ButtonNotUsedToAddDevice(int button, String idRoom, String topic, String type);
}
