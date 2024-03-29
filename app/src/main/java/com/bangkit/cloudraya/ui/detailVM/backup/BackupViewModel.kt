package com.bangkit.cloudraya.ui.detailVM.backup

import androidx.lifecycle.LiveData
import com.bangkit.cloudraya.repository.CloudRepository
import androidx.lifecycle.ViewModel
import com.bangkit.cloudraya.model.remote.BackupListResponse
import com.bangkit.cloudraya.model.local.Event

class BackupViewModel(private val repository: CloudRepository) : ViewModel() {

    fun getBackupList(token : String, siteUrl : String) : LiveData<Event<BackupListResponse>> = repository.getBackupList(token, siteUrl)

    fun deleteBackup(token: String, backupId: Int, vmId: Int) =
        repository.deleteBackup(token, backupId, vmId)

    fun restoreBackup(token: String, backupId: Int, vmId: Int) =
        repository.restoreBackup(token, backupId, vmId)

    fun getBackupConfig(token: String, vmId: Int) =
        repository.getBackupConfig(token, vmId)

    fun saveBackupConfig(token: String, vmId: Int, status: Int, days: Int, retentions: Int) =
        repository.saveBackupConfig(token, vmId, status, days, retentions)
}