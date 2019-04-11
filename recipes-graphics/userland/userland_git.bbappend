do_install_append () {
	for f in `find ${D}${includedir}/ -name "*.h"`; do
		sed -i 's/include "vchost_config.h"/include "linux\/vchost_config.h"/g' ${f}
	done
}
