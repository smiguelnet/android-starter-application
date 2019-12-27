package net.smiguel.app.config.injection.component;

import net.smiguel.app.App;
import net.smiguel.app.config.injection.module.AppModule;
import net.smiguel.app.config.injection.module.ContributesInjectorModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ContributesInjectorModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);

        AppComponent build();
    }

    void inject(App app);
}
