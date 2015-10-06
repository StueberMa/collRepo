grammar Sample;

arithmeticExpr
	: DECIMAL '+' DECIMAL  
 	| DECIMAL '-' DECIMAL
 	| DECIMAL '*' DECIMAL
 	| DECIMAL '/' DECIMAL;

DECIMAL
	: '-'?[0-9]+('.'[0-9]+)?;
 
WS 
	: [ \n\r\t\u000C]+ -> skip;