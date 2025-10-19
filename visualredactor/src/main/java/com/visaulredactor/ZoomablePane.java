package com.visaulredactor;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

/**
 * Pane з підтримкою масштабування (scroll) та панорамування (middle mouse/Shift+drag)
 */
public class ZoomablePane extends Pane {

    private final DoubleProperty scaleValue = new SimpleDoubleProperty(1.0);
    private double SCALE_DELTA = 1.1;
    private double MIN_SCALE = 0.1;
    private double MAX_SCALE = 5.0;

    private final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<>();
    private boolean isPanning = false;

    public ZoomablePane() {
        // Обмежуємо максимальний zoom до 100% (1.0)
        MIN_SCALE = 0.1;
        MAX_SCALE = 1.0;

        enableZoom();
        enablePan();
        setStyle("-fx-background-color: #2b2b2b;");
    }

    private void enableZoom() {
        this.addEventFilter(ScrollEvent.SCROLL, event -> {
            event.consume();

            double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
            double oldScale = getScaleX();
            double newScale = oldScale * scaleFactor;

            // Обмеження масштабу
            newScale = clamp(newScale, MIN_SCALE, MAX_SCALE);

            if (newScale == oldScale) {
                return; // Вже на межі
            }

            double f = (newScale / oldScale) - 1;
            Bounds bounds = localToScene(getBoundsInLocal());
            double dx = event.getSceneX() - (bounds.getWidth() / 2 + bounds.getMinX());
            double dy = event.getSceneY() - (bounds.getHeight() / 2 + bounds.getMinY());

            setScaleX(newScale);
            setScaleY(newScale);
            setTranslateX(getTranslateX() - f * dx);
            setTranslateY(getTranslateY() - f * dy);

            scaleValue.set(newScale);
        });
    }

    private void enablePan() {
        // Панування тільки на самому canvas (не на дочірніх елементах)
        this.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE || event.getButton() == MouseButton.SECONDARY) {
                isPanning = true;
                lastMouseCoordinates.set(new Point2D(event.getSceneX(), event.getSceneY()));
                event.consume();
            }
        });

        this.setOnMouseDragged(event -> {
            if (isPanning && lastMouseCoordinates.get() != null) {
                double deltaX = event.getSceneX() - lastMouseCoordinates.get().getX();
                double deltaY = event.getSceneY() - lastMouseCoordinates.get().getY();

                setTranslateX(getTranslateX() + deltaX);
                setTranslateY(getTranslateY() + deltaY);

                lastMouseCoordinates.set(new Point2D(event.getSceneX(), event.getSceneY()));
                event.consume();
            }
        });

        this.setOnMouseReleased(event -> {
            if (isPanning) {
                isPanning = false;
                lastMouseCoordinates.set(null);
                event.consume();
            }
        });
    }

    private double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    // Публічні методи для зовнішнього керування
    public void zoomIn() {
        setScale(scaleValue.get() * SCALE_DELTA);
    }

    public void zoomOut() {
        setScale(scaleValue.get() / SCALE_DELTA);
    }

    public void resetZoom() {
        scaleValue.set(1.0);
        setScaleX(1.0);
        setScaleY(1.0);
        setTranslateX(0);
        setTranslateY(0);
    }

    public void fitToContent() {
        // Центрувати контент
        Bounds bounds = getBoundsInLocal();
        setTranslateX(-bounds.getMinX());
        setTranslateY(-bounds.getMinY());
    }

    private void setScale(double newScale) {
        double clampedScale = clamp(newScale, MIN_SCALE, MAX_SCALE);
        scaleValue.set(clampedScale);
        setScaleX(clampedScale);
        setScaleY(clampedScale);
    }

    // Getters для налаштування
    public double getScale() {
        return scaleValue.get();
    }

    public DoubleProperty scaleProperty() {
        return scaleValue;
    }

    public void setScaleLimits(double min, double max) {
        this.MIN_SCALE = min;
        this.MAX_SCALE = max;
    }

    public void setScaleDelta(double delta) {
        this.SCALE_DELTA = delta;
    }
}