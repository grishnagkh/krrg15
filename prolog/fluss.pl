
can( move( Block, From, To), [ isAt( Block, From), pawnAt(From), isAt(wolf, W), isAt(goat,G), isAt(cabbage, C)] ) :-  
	is_block( Block), place( To), place( From), From \== To,
	( W = To ; C = To ; G = To ; Block = goat).

%move the pawn alone...
can( move(pawn, From, To),[pawnAt(From), isAt(wolf, To), isAt(cabbage, To), isAt(goat, From )] ):- place(From), place(To), From \= To.
can( move(pawn, From, To),[pawnAt(From), isAt(wolf, From), isAt(cabbage, From), isAt(goat,To )] )  :- place(From), place(To), From \= To.

% adds( Action, Relationships): Action establishes Relationships

adds( move(X,_,To), [ isAt(X,To), pawnAt(To)]).

% deletes( Action, Relationships): Action destroys Relationships

deletes( move(X,From,_), [ isAt(X,From), pawnAt(From), isAt(pawn, From)]).

% A blocks world

is_block( wolf ).
is_block( goat ).
is_block( cabbage ).

place( left).
place( right).

state1( [ isAt(wolf, left), isAt(goat, left), isAt(cabbage, left), pawnAt(left) ] ).
% state1(X), plan(X, [pawnAt(right), isAt(cabbage, right),isAt(wolf, right),isAt(goat, right)], F,C). 

