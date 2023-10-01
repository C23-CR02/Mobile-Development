package com.bangkit.cloudraya.di


import com.bangkit.cloudraya.model.local.DataHolder
import com.bangkit.cloudraya.model.local.VMHolder
import com.bangkit.cloudraya.repository.CloudRepository
import com.bangkit.cloudraya.ui.confirmation.ConfirmationActivityViewModel
import com.bangkit.cloudraya.ui.createVM.overview.CreateVm4ViewModel
import com.bangkit.cloudraya.ui.createVM.packages.CreateVm2ViewModel
import com.bangkit.cloudraya.ui.createVM.server.CreateVm1ViewModel
import com.bangkit.cloudraya.ui.createVM.vpc.CreateVm3ViewModel
import com.bangkit.cloudraya.ui.detailVM.DetailVMViewModel
import com.bangkit.cloudraya.ui.detailVM.backup.BackupViewModel
import com.bangkit.cloudraya.ui.detailVM.ip.IpViewModel
import com.bangkit.cloudraya.ui.detailVM.monitoring.MonitoringVMViewModel
import com.bangkit.cloudraya.ui.menu.dashboard.DashboardViewModel
import com.bangkit.cloudraya.ui.menu.dashboard.SharedViewModel
import com.bangkit.cloudraya.ui.menu.resources.ResourcesViewModel
import com.bangkit.cloudraya.ui.onboarding.OnBoardingViewModel
import com.bangkit.cloudraya.ui.resources.VMListViewModel
import com.bangkit.cloudraya.ui.siteAdd.SiteAddViewModel
import com.bangkit.cloudraya.ui.siteList.SiteListViewModel
import com.bangkit.cloudraya.ui.splashScreen.SplashScreenViewModel
import org.koin.dsl.module


val repositoryModule = module {
    single { CloudRepository(get(), get(), get(), get()) }
}

val dataModule = module {
    single { DataHolder() }
    single { VMHolder()}
}


val viewModule = module {
    single { DetailVMViewModel(get()) }
    single { VMListViewModel(get()) }
    single { SiteAddViewModel(get()) }
    single { SiteListViewModel(get()) }
    single { OnBoardingViewModel(get()) }
    single { SharedViewModel(get()) }
    single { SplashScreenViewModel(get()) }
    single { ConfirmationActivityViewModel(get()) }
    single { MonitoringVMViewModel(get()) }
    single { BackupViewModel(get()) }
    single { IpViewModel(get()) }
    single  { ResourcesViewModel(get())}
    single { DashboardViewModel(get())}
    single { CreateVm1ViewModel(get()) }
    single { CreateVm2ViewModel(get()) }
    single { CreateVm3ViewModel(get()) }
    single { CreateVm4ViewModel(get()) }

}
