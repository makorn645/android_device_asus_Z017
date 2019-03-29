# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, build/target/product/embedded.mk)

# Inherit from our custom product configuration
$(call inherit-product, vendor/omni/config/common.mk)

PRODUCT_NAME := omni_zenfone3
PRODUCT_DEVICE := zenfone3
PRODUCT_BRAND := asus
PRODUCT_MANUFACTURER := asus

PRODUCT_SYSTEM_PROPERTY_BLACKLIST := ro.product.name
