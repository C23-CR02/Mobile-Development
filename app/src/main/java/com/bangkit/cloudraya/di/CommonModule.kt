package com.bangkit.cloudraya.di


import com.bangkit.cloudraya.repository.CloudRepository
import com.bangkit.cloudraya.ui.DetailVM.DetailVMViewModel
import com.bangkit.cloudraya.ui.Home.HomeViewModel
import com.bangkit.cloudraya.ui.Resources.ResourcesViewModel
import com.bangkit.cloudraya.ui.SiteAdd.SiteAddViewModel
import com.bangkit.cloudraya.ui.SiteEdit.SiteEditViewModel
import com.bangkit.cloudraya.ui.SiteList.SiteListViewModel
import org.koin.dsl.module


val repositoryModule = module {
    single { CloudRepository(get(),get(),get()) }
}

val viewModule = module {
    single { DetailVMViewModel(get()) }
    single { ResourcesViewModel(get()) }
    single { SiteAddViewModel(get()) }
    single { SiteEditViewModel(get()) }
    single { SiteListViewModel(get()) }
    single { HomeViewModel(get()) }
}
