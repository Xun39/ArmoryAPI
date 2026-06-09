package net.xun.armory.platform.services;

import net.xun.armory.api.item.tools.ToolPieceType;

import java.util.stream.Stream;

public interface IToolCompatModule {

    String targetModId();

    Stream<ToolPieceType> toolPieces();
}
