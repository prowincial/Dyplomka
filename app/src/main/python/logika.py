import numpy as np
import pandas as pd
from scipy.stats import poisson
from os.path import dirname, join


SMALL = 0.00001
filename = join(dirname(__file__), "all2020.csv")
dframe2020 = pd.read_csv(filename)
# filename1 = join(dirname(__file__), "epl2021.csv")
# dframe2021 = pd.read_csv(filename1)


def kobra(frame, home_team ='Chelsea', away_team='Aston Villa'):

    home_frame = frame[frame['HomeTeam'] == home_team]
    home_team_matches = (len(home_frame))
    home_team_scored = (sum(home_frame['FTHG']))
    home_team_missed = (sum(home_frame['FTAG']))
    avg_home_team_scored = (home_team_scored / home_team_matches) + SMALL
    avg_home_team_missed = (home_team_missed / home_team_matches) + SMALL
    # print(avg_home_team_scored,avg_home_team_missed)

    guest_frame = frame[frame['AwayTeam'] == away_team]
    guest_team_matches = (len(guest_frame))
    guest_team_scored = (sum(guest_frame['FTAG']))
    guest_team_missed = (sum(guest_frame['FTHG']))
    avg_guest_team_scored = (guest_team_scored / guest_team_matches) + SMALL
    avg_guest_team_missed = (guest_team_missed / guest_team_matches) + SMALL
    # print(avg_home_team_scored, avg_guest_team_missed)

    matches = (len(frame))
    home_goals = (sum(frame['FTHG']))
    away_goals = (sum(frame['FTAG']))
    avg_home_goals = home_goals / matches
    avg_away_goals = away_goals / matches
    # print(avg_home_goals, avg_away_goals)

    home_a = avg_home_team_scored / avg_home_goals
    away_a = avg_guest_team_missed / avg_home_goals
    home_b = avg_home_team_missed / avg_away_goals
    away_b = avg_guest_team_scored / avg_away_goals
    home = home_a * away_a * avg_away_goals
    away = home_b * away_b * avg_away_goals
    # print(home, away)

    away_arr = poisson.pmf(mu=away, k=range(0, 6))
    home_arr = poisson.pmf(mu=home, k=range(0, 6))
    # print("Home team", home_arr)
    # print("Away team", away_arr)
    away_arr = away_arr.reshape(-1, 1)
    result = home_arr * away_arr
    # print(result)

    home_win = (np.sum(result[0, 1:5]) + np.sum(result[1, 2:5]) + np.sum(result[2, 3:5]) + np.sum(result[3, 4:5]) + result[4, 5])
    draw = (np.trace(result))
    away_win = 1 - (home_win + draw)

    # oz = (np.sum(result[1,1:5]) + np.sum(result[2,1:5]) +np.sum(result[3,1:5])+np.sum(result[4,1:5])+np.sum(result[5,1:5]))
    # print("Home (" + home_team + ')', round(home_win * 100), "% -", round((1 / home_win), 3))
    # print("X - ", round(draw*100), "% -", round((1/draw),3))
    # print("Away (" + away_team + ')', round(away_win * 100), "% -", round((1 / away_win), 3))
    # print(round((1/oz),3))
    return round((1 / home_win), 3), round((1/draw),3), round((1 / away_win), 3)

def kobra10(frame, home_team='Chelsea', away_team='Aston Villa'):

    new_frame = frame[(frame['HomeTeam'] == home_team) | (frame['AwayTeam'] == home_team)]
    new_frame = new_frame.tail(10)
    home_frame = new_frame[new_frame['HomeTeam'] == home_team]
    home_team_matches = (len(home_frame))
    home_team_scored = (sum(home_frame['FTHG']))
    home_team_missed = (sum(home_frame['FTAG']))
    avg_home_team_scored = (home_team_scored / home_team_matches) + SMALL
    avg_home_team_missed = (home_team_missed / home_team_matches) + SMALL
    # print(avg_home_team_scored,avg_home_team_missed)

    out_frame = frame[(frame['HomeTeam'] == away_team) | (frame['AwayTeam'] == away_team)]
    out_frame = out_frame.tail(10)
    guest_frame = out_frame[out_frame['AwayTeam'] == away_team]
    guest_team_matches = (len(guest_frame))
    guest_team_scored = (sum(guest_frame['FTAG']))
    guest_team_missed = (sum(guest_frame['FTHG']))
    avg_guest_team_scored = (guest_team_scored / guest_team_matches) + SMALL
    avg_guest_team_missed = (guest_team_missed / guest_team_matches) + SMALL
    # print(avg_home_team_scored, avg_guest_team_missed)

    matches = (len(frame))
    home_goals = (sum(frame['FTHG']))
    away_goals = (sum(frame['FTAG']))
    avg_home_goals = home_goals / matches
    avg_away_goals = away_goals / matches
    # print(avg_home_goals, avg_away_goals)

    home_a = avg_home_team_scored / avg_home_goals
    away_a = avg_guest_team_missed / avg_home_goals
    home_b = avg_home_team_missed / avg_away_goals
    away_b = avg_guest_team_scored / avg_away_goals
    home = home_a * away_a * avg_away_goals
    away = home_b * away_b * avg_away_goals
    # print(home, away)

    away_arr = poisson.pmf(mu=away, k=range(0, 6))
    home_arr = poisson.pmf(mu=home, k=range(0, 6))
    # print("Home team", home_arr)
    # print("Away team", away_arr)
    away_arr = away_arr.reshape(-1, 1)
    result = home_arr * away_arr
    # print(result)

    home_win = (np.sum(result[0, 1:5]) + np.sum(result[1, 2:5]) + np.sum(result[2, 3:5]) + np.sum(result[3, 4:5]) +
                result[4, 5])
    draw = (np.trace(result))
    away_win = 1 - (home_win + draw)

    # oz = (np.sum(result[1,1:5]) + np.sum(result[2,1:5]) +np.sum(result[3,1:5])+np.sum(result[4,1:5])+np.sum(result[5,1:5]))
    # print("Home (" + home_team + ')', round(home_win * 100), "% -", round((1 / home_win), 3))
    # print("X - ", round(draw*100), "% -", round((1/draw),3))
    # print("Away (" + away_team + ')', round(away_win * 100), "% -", round((1 / away_win), 3))
    # print(round((1/oz),3))
    return round((1 / home_win), 3), round((1 / draw), 3), round((1 / away_win), 3)

def result (a,b):
    koef1 = 0.25
    koef2 = 0.75

    res1 = kobra(dframe2020, a, b)
    res2 = kobra10(dframe2020, a, b)
    res = round((res1[0]*koef1 + res2[0]*koef2),3),\
          round((res1[1]*koef1 + res2[1]*koef2),3),\
          round((res1[2]*koef1 + res2[2]*koef2),3)
    return res