package com.pilasvacias.yaba.modules.emt;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.modules.emt.builders.EmtRequestManager;
import com.pilasvacias.yaba.modules.playa.VolleyModule;
import com.pilasvacias.yaba.screens.MainActivity;
import com.pilasvacias.yaba.screens.ProbaActivity;
import com.pilasvacias.yaba.screens.lineinfo.LineDestinationFragment;
import com.pilasvacias.yaba.screens.lineinfo.LineInfoActivity;
import com.pilasvacias.yaba.screens.lines.LineListFragment;
import com.pilasvacias.yaba.screens.lines.LinesFragment;
import com.pilasvacias.yaba.screens.search.SearchActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Pablo Orgaz - 10/21/13 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * The module for EMT. If you instance a subclass of {@link com.pilasvacias.yaba.core.network.NetworkFragment}
 * or {@link com.pilasvacias.yaba.core.network.NetworkActivity} declare it here as Injects.
 */
@Module(injects =
        {
                MainActivity.class,
                LinesFragment.class,
                LineListFragment.class,
                LineInfoActivity.class,
                LineDestinationFragment.class,
                ProbaActivity.class,
                SearchActivity.class,
        },
        includes = VolleyModule.class,
        library = true)
public class EmtModule {

    @Provides EmtEnvelopeSerializer provideEnvelopeSerializer() {
        return new EmtEnvelopeSerializer();
    }

    @Provides EmtRequestManager provideEmtRequestManager(RequestQueue requestQueue) {
        return new EmtRequestManager(requestQueue);
    }
}
