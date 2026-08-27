
% rules.pl - Reglas lógicas


% Importamos la base de conocimiento del otro archivo
:- consult('techsv.pl').

% Regla 1: Un lugar es "peligroso" si es exactamente el local donde está mamá o papá.
lugar_peligroso(Lugar, LocalMama, LocalPapa) :-
    Lugar = LocalMama ; Lugar = LocalPapa.

% Regla 2: Un lugar es "seguro" si existe en el centro comercial Y NO es peligroso.
% Usamos \+ que en Prolog significa negación (NOT).
lugar_seguro(Lugar, LocalMama, LocalPapa) :-
    local(Lugar),
    \+ lugar_peligroso(Lugar, LocalMama, LocalPapa).

% Regla 3: Regla principal de aridad 3 solicitada en el ejercicio.
% Argumento 1: Local de la mamá
% Argumento 2: Local del papá
% Argumento 3: Lista de locales donde Pepito puede estar.
% Usamos findall/3 para buscar todas las coincidencias de lugar_seguro y guardarlas en una lista.
esconder_pepito(LocalMama, LocalPapa, LugaresPermitidos) :-
    findall(Lugar, lugar_seguro(Lugar, LocalMama, LocalPapa), LugaresPermitidos).