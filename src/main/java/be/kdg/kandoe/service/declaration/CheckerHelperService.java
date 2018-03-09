package be.kdg.kandoe.service.declaration;

public interface CheckerHelperService<T> {
    boolean isItemNull(Long id);
    boolean isItemNull(String text);
}
