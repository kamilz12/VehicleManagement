package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDeserializer extends JsonDeserializer<List<FuelEconomyMenuItem>> {
    @Override
    public List<FuelEconomyMenuItem> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        List<FuelEconomyMenuItem> menuItems = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode itemNode : node) {
                FuelEconomyMenuItem menuItem = new FuelEconomyMenuItem();
                menuItem.setText(itemNode.get("text").asText());
                menuItem.setValue(itemNode.get("value").asText());
                menuItems.add(menuItem);
            }
        } else {
            FuelEconomyMenuItem menuItem = new FuelEconomyMenuItem();
            menuItem.setText(node.get("text").asText());
            menuItem.setValue(node.get("value").asText());
            menuItems.add(menuItem);
        }
        return menuItems;
    }
}