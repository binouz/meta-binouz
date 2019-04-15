# meta-binouz
Yocto recipes for personal projects

# Add to local.conf :

```
DISTRO_FEATURES += " \
    libc-backtrace \
    libc-big-macros \
    libc-bsd \
    libc-cxx-tests \
    libc-catgets \
    libc-charsets \
    libc-crypt \
    libc-crypt-ufc \
    libc-db-aliases \
    libc-envz \
    libc-fcvt \
    libc-fmtmsg \
    libc-fstab \
    libc-ftraverse \
    libc-getlogin \
    libc-idn \
    libc-inet-anl \
    libc-libm \
    libc-locales \
    libc-locale-code \
    libc-memusage \
    libc-nis \
    libc-nsswitch \
    libc-rcmd \
    libc-rtld-debug \
    libc-spawn \
    libc-streams \
    libc-sunrpc \
    libc-utmp \
    libc-utmpx \
    libc-wordexp \
    libc-posix-clang-wchar \
    libc-posix-regexp \
    libc-posix-regexp-glibc \
    libc-posix-wchar-io \
    ipv4 \
    ipv6 \
    opengl \
"

DISTRO_FEATURES_remove = "x11 mesa wayland pulseaudio ptest"

ENABLE_SPI_BUS="1"
```
