package com.bangkit.cloudraya.model.local

import java.io.Serializable

class DataHolder : Serializable {
    var title: String = ""
    var msgBody: String = ""
    var token: String = ""
    var request: String = ""
    var vmId: String = ""
    var siteUrl: String = ""
}