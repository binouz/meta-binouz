DESCRIPTION = "Abstraction library for lightweight browser development based on chromium using EGL"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit pkgconfig qemu

include gn-utils.inc

CHROMIUM_VERSION = "68.0.3440.91"

FILESEXTRAPATHS_append := "${THISDIR}/files/chromium-${CHROMIUM_VERSION}:"

DEPENDS = "bison-native gperf-native glib-2.0 libffi nss freetype fontconfig nspr-native nss-native qemu-native"
RDEPENDS_${PN} = "libudev freetype nss fontconfig libffi"

S = "${WORKDIR}/git"

SRC_URI  = "git://chromium.googlesource.com/chromium/tools/depot_tools.git;protocol=https;name=depot_tools;destsuffix=depot_tools"
SRC_URI += "git://git@github.com/binouz/chromium-egl-content.git;protocol=ssh;name=sources"
SRC_URI += "http://commondatastorage.googleapis.com/chromium-browser-official/chromium-${CHROMIUM_VERSION}.tar.xz;destsuffix=git/chromium/chromium-${CHROMIUM_VERSION};unpack=false"
SRC_URI += "file://Fix-conflicting-switches-when-compiling-for-arm.patch"
SRC_URI += "file://Fix-zlib-crc32-for-armv7-and-lower.patch"
SRC_URI += "file://v8-qemu-wrapper.patch"

SRCREV_depot_tools = "a28b14f122463a6d73c623e77c9dea4e228322dc"
SRCREV_sources = "9eba65913056bc355bae910fe98af3ff76e6d8f1"

SRC_URI[md5sum] = "fd303bd193a5a4cd82e3b0178b9184ff"
SRC_URI[sha256sum] = "1121c11ea732a3c4e090d5fb01e006516317f519f50b0defda022db31af7fcc6"

GN_EXTRA_ARGS = ' \
  custom_toolchain=\"//build/toolchain/yocto:yocto_target\" \
  v8_snapshot_toolchain=\"//build/toolchain/yocto:yocto_target\" \
  host_toolchain=\"//build/toolchain/yocto:yocto_native\" \
  target_cpu=\"arm\" \
  arm_version=7 \
  arm_tune=\"cortex-a7\" \
  arm_use_neon=true \
  use_system_zlib=true \
'

CHROMIUM_EXTRA_CFLAGS += "-Wno-error=attributes -Wno-error=class-memaccess -Wno-error=int-in-bool-context -Wno-error=packed-not-aligned -Wno-error=stringop-truncation -Wno-error=stringop-overflow -Wno-error=dangling-else"

do_unpack_extra() {
  mkdir -p ${S}/chromium/chromium-${CHROMIUM_VERSION}
  tar -xvf ${WORKDIR}/chromium-${CHROMIUM_VERSION}.tar.xz -C ${S}/chromium/
  export PATH="$PATH:${WORKDIR}/depot_tools"
  download_from_google_storage --no_resume --platform=linux* --no_auth --bucket chromium-gn -s ${S}/chromium/chromium-${CHROMIUM_VERSION}/buildtools/linux64/gn.sha1
  cd ${S} && oe_runmake prepare
}
addtask unpack_extra after do_unpack before do_patch

python do_write_toolchain_file () {
    """Writes a BUILD.gn file for Yocto detailing its toolchains."""
    toolchain_dir = d.expand("${S}/chromium/chromium-${CHROMIUM_VERSION}/build/toolchain/yocto")
    bb.utils.mkdirhier(toolchain_dir)
    toolchain_file = os.path.join(toolchain_dir, "BUILD.gn")
    write_toolchain_file(d, toolchain_file)
}
addtask write_toolchain_file after do_patch before do_configure


# V8's JIT infrastructure requires binaries such as mksnapshot and
# mkpeephole to be run in the host during the build. However, these
# binaries must have the same bit-width as the target (e.g. a x86_64
# host targeting ARMv6 needs to produce a 32-bit binary). Instead of
# depending on a third Yocto toolchain, we just build those binaries
# for the target and run them on the host with QEMU.
python do_create_v8_qemu_wrapper () {
    """Creates a small wrapper that invokes QEMU to run some target V8 binaries
    on the host."""
    qemu_libdirs = [d.expand('${STAGING_DIR_HOST}${libdir}'),
                    d.expand('${STAGING_DIR_HOST}${base_libdir}')]
    qemu_cmd = qemu_wrapper_cmdline(d, d.getVar('STAGING_DIR_HOST', True),
                                    qemu_libdirs)
    wrapper_path = d.expand('${B}/v8-qemu-wrapper.sh')
    with open(wrapper_path, 'w') as wrapper_file:
        wrapper_file.write("""#!/bin/sh

# This file has been generated automatically.
# It invokes QEMU to run binaries built for the target in the host during the
# build process.

%s "$@"
""" % qemu_cmd)
    os.chmod(wrapper_path, 0o755)
}
do_create_v8_qemu_wrapper[dirs] = "${B}"
addtask create_v8_qemu_wrapper after do_patch before do_configure

do_configure() {
  export PATH="${WORKDIR}/depot_tools:$PATH"
  GN_EXTRA_ARGS="${GN_EXTRA_ARGS}" make configure
}

do_compile() {
  export PATH="${WORKDIR}/depot_tools:$PATH"

  ninja ${PARALLEL_MAKE} -C ${S}/out egl_content_lib
}

do_install() {
  export PATH="${WORKDIR}/depot_tools:$PATH"
  oe_runmake export

  install -d ${D}${bindir}
  install -d ${D}${libdir}
  install -d ${D}${includedir}/eglcontent

  install -m 0644 ${S}/out/icudtl.dat ${D}${bindir}/icudtl.dat 
  install -m 0644 ${S}/out/snapshot_blob.bin ${D}${bindir}/snapshot_blob.bin
  install -m 0644 ${S}/out/v8_context_snapshot.bin ${D}${bindir}/v8_context_snapshot.bin
  install -m 0644 ${S}/out/natives_blob.bin ${D}${bindir}/natives_blob.bin
  install -m 0644 ${S}/out/egl_content.pak ${D}${bindir}/egl_content.pak

  install -m 0755 ${S}/out/libegl_content_lib.so ${D}${libdir}/libegl_content_lib.so

  install -m 0644 ${S}/out/eglcontent/* ${D}${includedir}/eglcontent
}

FILES_${PN} = " \
  ${bindir}/icudtl.dat \
  ${bindir}/snapshot_blob.bin \
  ${bindir}/v8_context_snapshot.bin \
  ${bindir}/natives_blob.bin \
  ${bindir}/egl_content.pak \
  ${libdir}/libegl_content_lib.so \
  ${includedir}/eglcontent \
"

FILES_${PN}-dev = ""
ALLOW_EMPTY_${PN}-dev = "0"
