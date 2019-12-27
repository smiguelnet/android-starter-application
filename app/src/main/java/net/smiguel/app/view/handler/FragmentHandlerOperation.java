package net.smiguel.app.view.handler;

import net.smiguel.app.view.fragment.BaseFragment;

public interface FragmentHandlerOperation {

    void addFragment(BaseFragment fragment);

    void addFragment(BaseFragment fragment, boolean addToBackStack);

    void addFragment(BaseFragment fragment, boolean addToBackStack, boolean validateCurrent);

    void addFragment(BaseFragment fragment, boolean addToBackStack, boolean validateCurrent, boolean clearBackStack);

    void popUp();

    void popUp(String name);

    void popUpAll();
}
