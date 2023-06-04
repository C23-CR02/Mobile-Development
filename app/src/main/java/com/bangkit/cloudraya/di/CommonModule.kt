package com.bangkit.cloudraya.di


import com.bangkit.cloudraya.ConfirmationActivityViewModel
import com.bangkit.cloudraya.model.local.DataHolder
import com.bangkit.cloudraya.repository.CloudRepository
import com.bangkit.cloudraya.ui.splashScreen.SplashScreenViewModel
import com.bangkit.cloudraya.ui.detailVM.DetailVMViewModel
import com.bangkit.cloudraya.ui.home.HomeViewModel
import com.bangkit.cloudraya.ui.onboarding.OnBoardingViewModel
import com.bangkit.cloudraya.ui.resources.ResourcesViewModel
import com.bangkit.cloudraya.ui.siteAdd.SiteAddViewModel
import com.bangkit.cloudraya.ui.siteEdit.SiteEditViewModel
import com.bangkit.cloudraya.ui.siteList.SiteListViewModel
import io.reactivex.schedulers.Schedulers.single
import org.koin.dsl.module


val repositoryModule = module {
    single { CloudRepository(get(), get(), get(), get()) }
}

val dataModule = module {
    single { DataHolder() }
}

val viewModule = module {
    single { DetailVMViewModel(get()) }
    single { ResourcesViewModel(get()) }
    single { SiteAddViewModel(get()) }
    single { SiteEditViewModel(get()) }
    single { SiteListViewModel(get()) }
    single { HomeViewModel(get()) }
    single { OnBoardingViewModel(get()) }
    single { SplashScreenViewModel(get()) }
    single { ConfirmationActivityViewModel(get()) }
}
