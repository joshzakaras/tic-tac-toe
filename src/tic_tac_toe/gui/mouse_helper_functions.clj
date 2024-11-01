(ns tic-tac-toe.gui.mouse-helper-functions)

(defn within-rect? [[rect-x rect-y rect-width rect-height] x y]
  (and
    (>= x rect-x)
    (<= x (+ rect-x rect-width))
    (>= y rect-y)
    (<= y (+ rect-y rect-height))))

(defn mouse-to-tile-coords [x y]
  {:row (int (Math/floor (/ y 100))) :column (int (Math/floor (/ x 100)))})