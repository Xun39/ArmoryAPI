package net.xun.armory.platform.services;

import net.xun.armory.platform.IToolCompatModule;

public interface IToolCompatProvider {

    String targetModId();

    IToolCompatModule create();
}
