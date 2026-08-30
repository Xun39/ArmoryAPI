package net.xun.armory.platform;

import net.xun.armory.api.item.tools.ToolPieceType;

import java.util.stream.Stream;

public interface IToolCompatModule {

    Stream<ToolPieceType> toolPieces();
}
