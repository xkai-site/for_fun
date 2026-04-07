package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "custom.order")
public class OrderFeatureProperties {

    private boolean featureFlag = true;
    private int maxCreateCount = 10;

    public boolean isFeatureFlag() {
        return featureFlag;
    }

    public void setFeatureFlag(boolean featureFlag) {
        this.featureFlag = featureFlag;
    }

    public int getMaxCreateCount() {
        return maxCreateCount;
    }

    public void setMaxCreateCount(int maxCreateCount) {
        this.maxCreateCount = maxCreateCount;
    }
}
