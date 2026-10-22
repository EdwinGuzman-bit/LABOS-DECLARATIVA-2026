% Calculadora de sumas y restas.
% Las reglas trabajan con la estructura de la expresion enviada por Java.

% Caso base: numero
evaluar(Expresion, Resultado) :-
    number(Expresion),
    Resultado is Expresion,
    !.

% Suma: se evalua cada lado y luego se suman los resultados.
evaluar(Izquierda + Derecha, Resultado) :-
    evaluar(Izquierda, ValorIzquierdo),
    evaluar(Derecha, ValorDerecho),
    Resultado is ValorIzquierdo + ValorDerecho,
    !.

% Resta: se evalua cada lado y luego se restan los resultados.
evaluar(Izquierda - Derecha, Resultado) :-
    evaluar(Izquierda, ValorIzquierdo),
    evaluar(Derecha, ValorDerecho),
    Resultado is ValorIzquierdo - ValorDerecho,
    !.

% Numero negativo, por ejemplo -5 o -(3+2).
evaluar(-Expresion, Resultado) :-
    evaluar(Expresion, Valor),
    Resultado is -Valor,
    !.
