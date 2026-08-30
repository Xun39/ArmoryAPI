package net.xun.armory.platform;

import net.xun.armory.ArmoryConstants;
import net.xun.armory.platform.services.IPlatformHelper;
import net.xun.armory.platform.services.IToolCompatProvider;

import java.util.*;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final List<IToolCompatModule> ACTIVE_COMPAT_MODULES = loadActiveCompatModules();

    public static List<IToolCompatModule> loadActiveCompatModules() {
        List<IToolCompatModule> services = new ArrayList<>();

        ServiceLoader.load(IToolCompatProvider.class)
                .stream()
                .forEach(provider -> {
                    IToolCompatProvider compatProvider = provider.get();
                    String modId = compatProvider.targetModId();

                    ArmoryConstants.LOG.debug("Found compat module provider {} targeting mod {}", compatProvider.getClass().getName(), modId);

                    if (!PLATFORM.isModLoaded(modId)) {
                        ArmoryConstants.LOG.debug("Skipping compat module {} because mod {} is not loaded", compatProvider.getClass().getName(), modId);
                        return;
                    }

                    IToolCompatModule module = compatProvider.create();
                    services.add(module);

                    ArmoryConstants.LOG.debug(
                            "Loaded compat module {} for mod {}",
                            module.getClass().getName(),
                            modId
                    );
                });

        ArmoryConstants.LOG.debug(
                "Loaded {} active compat module implementations for service {}",
                services.size(),
                IToolCompatModule.class.getName()
        );
        for (IToolCompatModule service : services) {
            ArmoryConstants.LOG.debug(" - {}", service.getClass().getName());
        }

        return services;
    }

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));

        ArmoryConstants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}