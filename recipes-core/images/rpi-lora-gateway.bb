include recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += " \
	kernel-modules \
"

IMAGE_INSTALL_append += " \
        e2fsprogs \
        tzdata \
        tzdata-misc \
        tzdata-africa \
        tzdata-americas \
        tzdata-antarctica \
        tzdata-arctic \
        tzdata-asia \
        tzdata-atlantic \
        tzdata-australia \
        tzdata-europe \
        tzdata-pacific \
	linux-firmware-bcm43430 \
	lshw \
	liberation-fonts \
	wiringpi \
"

IMAGE_FEATURES_append += " \
        package-management \
        tools-debug \
        ssh-server-openssh \
"

ENABLE_SPI_BUS="1"

GLIBC_GENERATE_LOCALES = "fr_FR-UTF.8"
LINGUAS = "fr-fr"
