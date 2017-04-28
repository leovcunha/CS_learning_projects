(define (caar x) (car (car x)))
(define (cadr x) (car (cdr x)))
(define (cddr x) (cdr (cdr x)))
(define (cadar x) (car (cdr (car x))))

; Some utility functions that you may find useful to implement.
(define (map proc items)
  (if (null? items) nil (cons (proc (car items)) (map proc (cdr items)))))

(define (cons-all first rests)
  (cond
    ((null? rests) nil)
    (else (cons
      (append (cons first nil) (car rests))
      (cons-all first (cdr rests))))))

(define (zip pairs)
  (list (map car pairs) (map cadr pairs)))

;; Problem 18
;; Returns a list of two-element lists
(define (enumerate s)
   (define (enum s index)
    (if (null? s)
      nil
      (cons (cons index (cons (car s) nil)) (enum (cdr s) (+ index 1)))
    )
  )
  (enum s 0)
  )
  ; END Question 18

;; Problem 19
;; List all ways to make change for TOTAL with DENOMS
(define (list-change total denoms)
  ; BEGIN Question 19
    (cond ((null? denoms) nil)  ;impossible to partition
        ((< total 0) nil)     ;impossible to partition
        ((= total 0) (cons nil nil))  ;one way to partition - empty
        ((< total (car denoms)) (list-change total (cdr denoms)))
        (else (define with-max-denom (cons-all (car denoms) (list-change (- total (car denoms)) denoms)))
              (define without-max-denom (list-change total (cdr denoms)))
              (append with-max-denom without-max-denom)
          )
    )
  


)
   ; END Question 19

;; Problem 20
;; Returns a function that checks if an expression is the special form FORM
(define (check-special form)
  (lambda (expr) (equal? form (car expr))))

(define lambda? (check-special 'lambda))
(define define? (check-special 'define))
(define quoted? (check-special 'quote))
(define let?    (check-special 'let))

;; Converts all let special forms in EXPR into equivalent forms using lambda
(define (analyze expr)
  (cond ((atom? expr)
         ; BEGIN Question 20
         expr
         ; END Question 20
         )
        ((quoted? expr)
         ; BEGIN Question 20
         expr
         ; END Question 20
         )
        ((or (lambda? expr)
             (define? expr))
         (let ((form   (car expr))
               (params (cadr expr))
               (body   (cddr expr)))
           ; BEGIN Question 20
           (cons form (cons params (map analyze body)))
           ; END Question 20
           ))
        ((let? expr)
         (let ((values (cadr expr))
               (body   (cddr expr)))
           ; BEGIN Question 20
           (cons (cons 'lambda (cons (car (zip values)) (map analyze body))) (map analyze (cadr (zip values))))
           ; END Question 20
           ))
        (else
         ; BEGIN Question 20
         (cons (car expr) (map analyze (cdr expr)))
         ; END Question 20
         )))

;; Problem 21 (optional)
;; Draw the hax image using turtle graphics.
(define (hax d k)
  ; BEGIN Question 21
  'REPLACE-THIS-LINE
  )
  ; END Question 21

