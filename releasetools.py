# Copyright (C) 2009 The Android Open Source Project
# Copyright (c) 2011, The Linux Foundation. All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import hashlib
import common
import re

def FullOTA_Assertions(info):
  AddBootloaderAssertion(info)
  AddModemAssertion(info)
  AddTrustZoneAssertion(info)
  return

def IncrementalOTA_Assertions(info):
  AddBootloaderAssertion(info)
  AddModemAssertion(info)
  AddTrustZoneAssertion(info)
  return

def AddBootloaderAssertion(info):
  android_info = info.input_zip.read("OTA/android-info.txt")
  m = re.search(r'require\s+version-bootloader\s*=\s*(.+)', android_info)
  if m:
    versions = m.group(1).split('|')
    if len(versions) and '*' not in versions:
      cmd = 'assert(msm8953.verify_bootloader(' + ','.join(['"%s"' % bootloader for bootloader in versions]) + ') == "1");'
      info.script.AppendExtra(cmd)
  return

def AddModemAssertion(info):
  android_info = info.input_zip.read("OTA/android-info.txt")
  m = re.search(r'require\s+version-modem\s*=\s*(.+)', android_info)
  if m:
    versions = m.group(1).split('|')
    if len(versions) and '*' not in versions:
      cmd = 'assert(msm8953.verify_modem(' + ','.join(['"%s"' % modem for modem in versions]) + ') == "1");'
      info.script.AppendExtra(cmd)
  return

def AddTrustZoneAssertion(info):
  android_info = info.input_zip.read("OTA/android-info.txt")
  m = re.search(r'require\s+version-trustzone\s*=\s*(.+)', android_info)
  if m:
    versions = m.group(1).split('|')
    if len(versions) and '*' not in versions:
      cmd = 'assert(msm8953.verify_trustzone(' + ','.join(['"%s"' % trustzone for trustzone in versions]) + ') == "1");'
      info.script.AppendExtra(cmd)
  return
