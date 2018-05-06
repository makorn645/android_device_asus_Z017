# Inherit device configuration
$(call inherit-product, device/asus/zenfone3/device.mk)

PRODUCT_NAME := omni_zenfone3
PRODUCT_DEVICE := zenfone3
PRODUCT_BRAND := asus
PRODUCT_MANUFACTURER := asus

PRODUCT_SYSTEM_PROPERTY_BLACKLIST := ro.product.name
