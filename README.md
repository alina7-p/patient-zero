# Assignment 4. Patient Zero

Student ID: 253441
Name: Alina Ibadulla
Group: SE-2540

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 103
    Shared core: 1066
    Marker hits: 4
    First hit: 13054
    Border length: 3
    Shared words: 342
    Tests needed: 42
    Cheapest testing: 163
    Longest chain: 37

My outbreak report (a screenshot of `output/outbreak.html`):

<img width="1419" height="821" alt="Screenshot 2026-11-09 at 13 50 06" src="https://github.com/user-attachments/assets/b8b70d77-7556-4a13-9a37-15891c384359" />


## Questions

Answer in your own words, two or three sentences each.

Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?

The cell d[i][j] is the smallest number of changes (replace, insert, delete) that turn the first i letters of A into the first j letters of B. It is computed from three smaller cells: the diagonal d[i-1][j-1] (plus 0 if the letters are equal, plus 1 if not), the cell above d[i-1][j] + 1 (delete), and the cell on the left d[i][j-1] + 1 (insert). The smallest of the three is taken. The first row and column are j and i, because turning a word into an empty word, or the other way round, costs its length.

Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?

A common substring must be one continuous piece in both strains, but a common subsequence may skip letters, as long as the order stays the same. My Shared core (1066) is bigger than the number of letters that are equal at the same position because after an insertion or deletion the rest of strain B is shifted. The shifted letters no longer line up by position, but the subsequence still matches them in order.

Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?

The prefix function pi[i] tells how long the longest proper prefix of the marker is that is also a suffix of its first i+1 letters. After a mismatch, this is how much of the current match is still useful. The text pointer never goes back because on a mismatch we only shorten the match length k with k = pi[k-1] and keep the same text letter. The letters before it are already known to match, so nothing has to be read again. My marker is TGTGTATGT and its Border length is 3. This means its first 3 letters (TGT) are the same as its last 3 letters, so two hits can overlap by 3 letters, and after a full match KMP continues with k = 3 instead of 0.

Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?

Two different words can have the same hash (a collision), so equal hashes only mean “probably equal”. Only comparing the letters shows that the words are really the same, and without it the count would include false alarms. If many windows have the same hash, every window needs a comparison of k letters with many candidates, so the time grows from about O(n + m) toward O(n·m·k) in the worst case.

Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?

For each person v there are two states: take[v], the cheapest cost in the subtree of v if v is tested, and skip[v], the cheapest cost if v is not tested. take[v] = cost[v] + the sum over children of min(take[c], skip[c]), because the links to the children are already covered. skip[v] = the sum of take[c] over children, because if v is not tested every child must be. The answer is min(take[0], skip[0]) at the root. Greedy “test the person with most links first” fails because it looks only at one person and ignores costs and what happens below. For example, a hub with a high cost may be worse than testing several cheap neighbours, while the DP compares both choices for every person and keeps the best.

## AI use

The syllabus requires you to say which AI tools you used and for what. Write the tool (ChatGPT, Copilot, other) and what you asked it: explain an idea, find an error, review code. Write "none" if you did not use any. If you took code or text from a book or a website, give the source here.

Claude explained the algorithms and helped with code that I tested and studied

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time                      |
|---|---|---------------------------|
| naive search | 20 000 / 40 000 / 80 000 | 31.0 / 72.7 / 148.9 ms    |
| KMP | 20 000 / 40 000 / 80 000 | 2.0 / 0.8 / 0.3 ms        |
| edit distance | 500 / 1000 / 2000 / 4000 | 5.8 / 5.4 / 16.4 / 65.6 ms |

What do the numbers say about how the time grows? When you double the size, by how much does each time grow? Two or three sentences.

The naive search grows about 2 times when the text doubles (31 to 73 to 149 ms), because at each position it compares up to about 2000 letters, so its cost is roughly text length times pattern length. KMP stays at 0.3 to 2 ms because it reads each letter of the text once and never goes back. Its times are so small that Java warm-up and noise dominate, so they show no real trend. The edit distance grows about 4 times when the length doubles (16.4 ms to 65.6 ms), which matches the n·m cells of the table. The two smallest sizes are not a trend, since the first runs are slow while Java warms up.

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm | time                                 | space  | assumptions                                                                                              |
|---|--------------------------------------|--------|----------------------------------------------------------------------------------------------------------|
| Edit distance | O(n·m)                               | O(n·m) | Full table of (n+1)·(m+1) cells, each filled in O(1). Space could be O(m) if only two rows are kept, but my code keeps the whole table. |
| LCS | O(n·m)                               | O(n·m) | Same table size and the same O(1) work per cell.                                                         |
| Prefix function and KMP | O(m) for the prefix function, O(n + m) for KMP | O(m)   | The text pointer only moves forward, and the match length k falls back at most as many times as it grew. |
| Rabin-Karp (shared words) | O(n + m) on avg, O(n·m·k) worst case | O(m)       | The hash is rolled in O(1) per step. Average case: few collisions and a hash map lookup in O(1). Worst case: many equal hashes, so each window is compared letter by letter with many candidates. |
| Tree DP | O(v)                                     | O(v)       | Each person is visited once, and each link is looked at a constant number of times. Recursion depth is up to v, which is fine for about 100 people. The longest chain is also O(v).|

## Before you submit

- The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
