include recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += " \
	kernel-modules \
"

IMAGE_INSTALL_append += " \
        e2fsprogs \
        wpa-supplicant \
        connman \
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
	rpi-display-manager \
	rpi-chromium-egl-content-launcher \
	liberation-fonts \
"

IMAGE_FEATURES_append += " \
        package-management \
        tools-debug \
        ssh-server-openssh \
"

DISTRO_FEATURES_append += " \
	wifi \
"

PACKAGECONFIG_append_pn-connman = " wifi"

GLIBC_GENERATE_LOCALES = "fr_FR-UTF.8"
LINGUAS = "fr-fr"
