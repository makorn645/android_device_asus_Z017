/*
   Copyright (c) 2014, The Linux Foundation. All rights reserved.
   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are
   met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of The Linux Foundation nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.
   THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
   WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
   ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
   BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
   CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
   SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
   BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
   WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
   OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
   IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#include <stdlib.h>

#define _REALLY_INCLUDE_SYS__SYSTEM_PROPERTIES_H_
#include <sys/_system_properties.h>
#include <sys/stat.h>

#include <android-base/logging.h>
#include <android-base/properties.h>
#include "property_service.h"

#include <fcntl.h>
#include <unistd.h>

namespace android {
namespace init {

void property_override(char const prop[], char const value[])
{
    prop_info *pi;

    pi = (prop_info*) __system_property_find(prop);
    if (pi)
        __system_property_update(pi, value, strlen(value));
    else
        __system_property_add(prop, strlen(prop), value, strlen(value));
}

static void set_serial()
{
    int fd, rc;
    char buf[16];
    int status = 1;
    const char *path = "/factory/SSN";

    fd = open(path, O_RDONLY);
    if (fd < 0) {
        status = -1;
    }

    if (rc = read(fd, buf, 15) < 0) {
        status = -1;
    } else {
        buf[15] = '\0';
        property_override("ro.serialno", buf);
    }
    close(fd);

    if (status < 0) {
        property_override("ro.serialno", "UNKNOWNSERIALNO");
    }
}

void vendor_load_properties()
{
    set_serial();

    int project = stoi(android::base::GetProperty("ro.boot.id.prj", ""));
    property_set("ro.product.name", "WW_Phone");
    if (project == 6) {
        property_override("ro.build.product", "ZE520KL");
        property_override("ro.build.description", "WW_Phone-user 8.0.0 OPR1.170623.026 15.0410.1712.31-0 release-keys");
        property_override("ro.build.fingerprint", "asus/WW_Phone/ASUS_Z017D:8.0.0/OPR1.170623.026/15.0410.1712.31-0:user/release-keys");
        property_override("ro.product.device", "ASUS_Z017D_1");
        property_override("ro.product.model", "ASUS_Z017D");
        property_set("ro.product.carrier", "US-ASUS_Z017D-WW_Phone");
        property_set("ro.hardware.id", "ZE520KL_MP");
        property_set("ro.build.csc.version", "WW_ZE520KL-15.0410.1712.31-0");
    } else if (project == 7) {
        property_override("ro.build.product", "ZE552KL");
        property_override("ro.build.description", "WW_Phone-user 8.0.0 OPR1.170623.026 15.0410.1712.31-0 release-keys");
        property_override("ro.build.fingerprint", "asus/WW_Phone/ASUS_Z012D:8.0.0/OPR1.170623.026/15.0410.1712.31-0:user/release-keys");
        property_override("ro.product.device", "ASUS_Z012D");
        property_override("ro.product.model", "ASUS_Z012D");
        property_set("ro.product.carrier", "US-ASUS_Z012D-WW_Phone");
        property_set("ro.hardware.id", "ZE552KL_MP");
        property_set("ro.build.csc.version", "WW_ZE552KL-15.0410.1712.31-0");
    }
}
}
}
