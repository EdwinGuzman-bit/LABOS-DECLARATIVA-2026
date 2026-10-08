% Laboratorio 04 - Listas en Prolog
% Ejercicio: Almacenar cada digito de un numero en una lista.
%
% Ejemplo:
% ?- almacenar(82671, L).
% L = [1, 7, 6, 2, 8].

almacenar(N, [N]) :-
    N < 10.

almacenar(N, [D|R]) :-
    N >= 10,
    D is N mod 10,
    N1 is N // 10,
    almacenar(N1, R).
