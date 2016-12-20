/*
 * Copyright (c) 2015-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencypher.tools.grammar;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.tool.ast.GrammarRootAST;
import org.junit.Test;

public class Antlr4ParserTest
{

    @Test
    public void shouldParseValidCypher() throws FileNotFoundException, URISyntaxException
    {
        getQueries( "/cypher.txt" ).forEach( Antlr4TestUtils::parse );
    }

    @Test
    public void shouldParseLegacyCypher() throws FileNotFoundException, URISyntaxException
    {
        getQueries( "/cypher-legacy.txt" ).forEach( Antlr4TestUtils::parseLegacy );
    }

//    @Test
    public void investigateTokenStream() throws IOException
    {
        // Keep: Not really testing things but quite useful for debugging antlr lexing
        String query = "CREATE (a)";
        org.antlr.v4.Tool tool = new org.antlr.v4.Tool();

        GrammarRootAST ast = tool.parseGrammarFromString( new String( Files.readAllBytes(Paths.get("/Users/mats/gitRoots/openCypher/grammar/generated/Cypher.g4"))) );
        org.antlr.v4.tool.Grammar g = tool.createGrammar( ast );
        tool.process( g, false );

        LexerInterpreter lexer = g.createLexerInterpreter( new ANTLRInputStream( query ) );
        CommonTokenStream tokenStream = new CommonTokenStream( lexer );
    }

    private List<String> getQueries( String queryFile ) throws FileNotFoundException, URISyntaxException
    {
        URL resource = getClass().getResource( queryFile );
        Scanner scanner = new Scanner( new FileReader( Paths.get( resource.toURI() ).toFile() ) );
        scanner.useDelimiter( "§\n" );
        ArrayList<String> queries = new ArrayList<>();
        while ( scanner.hasNext() )
        {
            queries.add( scanner.next() );
        }
        return queries;
    }
}
