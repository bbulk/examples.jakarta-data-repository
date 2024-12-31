package dev.bbulk;

import jakarta.data.page.PageRequest;

import java.util.Optional;

public record Pagination(long page, int size, boolean requestTotal) implements PageRequest {

    @Override
    public PageRequest afterCursor(Cursor cursor) {
        return null;
    }

    @Override
    public PageRequest beforeCursor(Cursor cursor) {
        return null;
    }

    @Override
    public Optional<Cursor> cursor() {
        return Optional.empty();
    }

    @Override
    public Mode mode() {
        return Mode.OFFSET;
    }

    @Override
    public PageRequest size(int maxPageSize) {
        return null;
    }

    @Override
    public PageRequest withoutTotal() {
        return new Pagination(page, size, false);
    }

    @Override
    public PageRequest withTotal() {
        return new Pagination(page, size, true);
    }
}
