package com.reus.algorithms2.baseballelimination;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php
 *
 * @author Konstantin Reus
 * @since 07.12.2020
 */
public class BaseballElimination {
    private final int numberOfTeams;
    private final String[] teams;
    private final int[] wins;
    private final int[] loss;
    private final int[] left;
    private final int[][] games;
    private final int n;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = Integer.parseInt(in.readLine());
        n = numberOfTeams;
        teams = new String[numberOfTeams];
        wins = new int[numberOfTeams];
        loss = new int[numberOfTeams];
        left = new int[numberOfTeams];
        games = new int[numberOfTeams][numberOfTeams];
        int i = 0;
        while (in.hasNextLine()) {
            String s = in.readLine();
            String[] splitted = s.trim().split("\\s+");
            teams[i] = splitted[0];
            wins[i] = Integer.parseInt(splitted[1]);
            loss[i] = Integer.parseInt(splitted[2]);
            left[i] = Integer.parseInt(splitted[3]);
            for (int j = 0; j < splitted.length - 4; j++) {
                games[i][j] = Integer.parseInt(splitted[j + 4]);
            }
            i++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return new ArrayList<>(Arrays.asList(teams));
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        return wins[getIndexByTeamName(team)];
    }

    private int getIndexByTeamName(String teamName) {
        for (int i = 0; i < teams.length; i++) {
            String team = teams[i];
            if (team.equals(teamName)) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        return loss[getIndexByTeamName(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        return left[getIndexByTeamName(team)];
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException();
        }
        return games[getIndexByTeamName(team1)][getIndexByTeamName(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        int i = getIndexByTeamName(team);
        if (n == 1) {
            return false;
        }
        for (int j = 0; j < wins.length; j++) {
            if (j == i) {
                continue;
            }
            if (wins[j] > wins[i] + left[i]) {
                return true;
            }
        }
        if (n == 2) {
            return false;
        }
        FlowNetwork fn = buildFlowNetwork(i);
        int game = n * (n - 1) / 2;
        int s = n + game;
        int t = s + 1;
        FordFulkerson ff = new FordFulkerson(fn, s, t);
        for (FlowEdge sourceEdge : fn.adj(s)) {
            if (sourceEdge.flow() != sourceEdge.capacity()) {
                return true;
            }
        }
        return false;
    }

    private FlowNetwork buildFlowNetwork(int target) {
        int game = n * (n - 1) / 2;
        int V = n + game + 2;
        int s = n + game;
        int t = s + 1;
        int gameVertice = 0;
        FlowNetwork fn = new FlowNetwork(V);
        for (int v = 0; v < n; v++) {
            for (int w = v + 1; w < n; w++) {
                FlowEdge sTo = new FlowEdge(s, gameVertice, games[v][w]);
                FlowEdge gameTo1 = new FlowEdge(gameVertice, game + v, Double.POSITIVE_INFINITY);
                FlowEdge gameTo2 = new FlowEdge(gameVertice, game + w, Double.POSITIVE_INFINITY);
                fn.addEdge(sTo);
                fn.addEdge(gameTo1);
                fn.addEdge(gameTo2);
                gameVertice++;
            }
            FlowEdge toT = new FlowEdge(game + v, t, wins[target] + left[target] - wins[v]);
            fn.addEdge(toT);
        }
        return fn;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!isEliminated(team)) {
            return null;
        }
        int target = getIndexByTeamName(team);
        Stack<String> certificate = new Stack<>();
        if (isEliminated(team)) {
            for (int v = 0; v < n; v++) {
                if (wins[target] + left[target] - wins[v] < 0) {
                    certificate.push(teams[v]);
                    return certificate;
                }
            }
            FlowNetwork fn = buildFlowNetwork(target);
            int game = n * (n - 1) / 2;
            int s = n + game;
            int t = s + 1;
            FordFulkerson ff = new FordFulkerson(fn, s, t);
            for (int i = game; i < s; i++) {
                if (ff.inCut(i)) {
                    String name = teams[i - game];
                    certificate.push(name);
                }
            }
            return certificate;
        }
        return null;
    }
}
