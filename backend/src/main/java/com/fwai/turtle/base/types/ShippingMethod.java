package com.fwai.turtle.base.types;

public enum ShippingMethod {
    EXPRESS("快递"),
    SELF_DELIVERY("自送");
    
    private final String description;

    ShippingMethod(String description) {
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public static ShippingMethod get(String name) {
        for (ShippingMethod e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
