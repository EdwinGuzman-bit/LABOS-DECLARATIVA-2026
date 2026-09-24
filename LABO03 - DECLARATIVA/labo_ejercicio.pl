
% Caso base: la suma "hasta 1" de 1 es 1.
suma(1, 1).

% Caso recursivo: se suma N con la suma de todos los
suma(N, Total) :-
    N > 1,
    N1 is N - 1,
    suma(N1, Total1),
    Total is N + Total1.

