package com.zizzle.cmpt370.Model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Static class used to read/write from database
 */
public class Storage {
    /** Reference to the database */
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    /**
     * Adds the input league to the database
     * @param newLeague: League object to be added to the database, the name of this league must be
     *                 unique
     */
    public static void writeLeague(League newLeague){
        LeagueInfo newLeagueInfo = new LeagueInfo(newLeague);
        // assume this league has a unique name, write it to the database
        database.child("Leagues").child(newLeagueInfo.getDatabaseKey()).setValue(newLeague);
    }


    /**
     * Adds the input team to the database
     * @param newTeam: Team to be added to the database
     */
    public static void writeTeam(Team newTeam){
        // assume newTeam already has a unique name for the league it's in
        TeamInfo newTeamInfo = new TeamInfo(newTeam);
        // write the team to the database
        database.child("Teams").child(newTeamInfo.getDatabaseKey()).setValue(newTeam);

    }


    /**
     * Adds the input member to the database,
     * @param member: Member object to be added to the database
     */
    public static void writeMember(Member member){
        // new member is added at the path /Users/memberEmail/
        // assume the member being added has a unique email address
        database.child("users").child(member.getUserID()).setValue(member);
    }


    /**
     * Adds the input team to the input league on the database, this assumes that the input team
     * can be validly added to the input league, and that both league and team are already on the database
     * @param parentLeagueInfo: LeagueInfo object the team is being added to
     * @param newTeamInfo: TeamInfo object to be added to the league
     */
    public static void addTeamToLeague(LeagueInfo parentLeagueInfo, TeamInfo newTeamInfo){
        // assume the input team is valid to add to this league
        // add the team to the league, and the league to the team
        database.child("Leagues").child(parentLeagueInfo.getDatabaseKey()).child("teamsInfoMap").child(newTeamInfo.getName()).setValue(newTeamInfo);
        // we don't have to add the league to the team, as the team already keeps track of its parent league
    }

    /**
     * Adds the input member to the input team and vice versa on the database, this assumes that the input
     * member can and should be added to the input team
     * @param teamMemberInfo: MemberInfo object describing the member to be added to the input team
     * @param newTeamInfo: TeamInfo object describing the team to add the input member to
     */
    public static void addTeamToMember(MemberInfo teamMemberInfo, TeamInfo newTeamInfo){
        // add the team to the member
        database.child("users").child(teamMemberInfo.getDatabaseKey()).child("teamInfoMap").child(newTeamInfo.getDatabaseKey()).setValue(newTeamInfo);
        // add the member to the team
        database.child("Teams").child(newTeamInfo.getDatabaseKey()).child("membersInfoMap").child(teamMemberInfo.getDatabaseKey()).setValue(teamMemberInfo);
    }

    /**
     * Removes the input member from the input team on the database, this is a no-op if the member isn't on the input team
     * @param memberInfo: MemberInfo object representing the member to remove from the team
     * @param teamInfo: TeamInfo object representing the team to remove from the member
     */
    public static void removeMemberFromTeam(MemberInfo memberInfo, TeamInfo teamInfo){
        // remove the team from the member
        database.child("users").child(memberInfo.getDatabaseKey()).child("teamInfoMap").child(teamInfo.getDatabaseKey()).removeValue();
        // remove the member from the team
        database.child("Teams").child(teamInfo.getDatabaseKey()).child("membersInfoMap").child(memberInfo.getDatabaseKey()).removeValue();
    }


}
