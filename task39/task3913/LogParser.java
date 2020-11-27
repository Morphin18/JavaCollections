package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.*;

import java.io.*;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
    private final Path logDir;
    private List<LogFile> logLines = new ArrayList<>();
    private Set<String> uniqueIps;

    public LogParser(Path logDir) {
        this.logDir = logDir;
        getLogs(this.logDir);
    }

    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                result.add(log.getIp());
            }
        }
        return result;
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (user.equals(log.getUserName())) {
                    result.add(log.getIp());
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (event.equals(log.getEvent())) {
                    result.add(log.getIp());
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (status.equals(log.getStatus())) {
                    result.add(log.getIp());
                }
            }
        }
        return result;
    }

    public void getLogs(Path logDir) {
        LogFile files = new LogFile();
        for (File file : logDir.toFile().listFiles()) {
            if (file.isFile() && file.getName().endsWith(".log")) {
                files.logFile.add(file);
            }
        }
        assert files.logFile != null;
        for (File file : files.logFile) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while (reader.ready()) {
                    String[] params = getParams(reader.readLine());
                    if (params.length == 5) {
                        logLines.add(new LogFile(params[0], params[1], readDate(params[2]), readEvent(params[3]), readStatus(params[4])));
                    } else {
                        logLines.add(new LogFile(params[0], params[1], readDate(params[2]), readEvent(params[3]), Integer.parseInt(params[4]), readStatus(params[5])));
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error IOException");
                e.getStackTrace();
            }
        }
    }

    private String[] getParams(String reader) {
        String[] params = reader.split("\t");
        if (readEvent(params[3]) == Event.DONE_TASK || readEvent(params[3]) == Event.SOLVE_TASK) {
            String[] result = new String[params.length + 1];
            result[0] = params[0];
            result[1] = params[1];
            result[2] = params[2];
            result[3] = params[3].split(" ")[0];
            result[4] = params[3].split(" ")[1];
            result[5] = params[4];
            return result;
        }
        return params;

    }

    private Event readEvent(String event) {
        switch (event) {
            case "DONE_TASK":
                return Event.DONE_TASK;
            case "DOWNLOAD_PLUGIN":
                return Event.DOWNLOAD_PLUGIN;
            case "LOGIN":
                return Event.LOGIN;
            case "SOLVE_TASK":
                return Event.SOLVE_TASK;
            case "WRITE_MESSAGE":
                return Event.WRITE_MESSAGE;
            default:
                String[] task = event.split(" ");
                if (task[0].equals(Event.SOLVE_TASK.name())) {
                    return Event.SOLVE_TASK;
                }
                if (task[0].equals(Event.DONE_TASK.name())) {
                    return Event.DONE_TASK;
                }
                return null;
        }
    }

    private Date readDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("d.M.yyyy H:m:s");
        try {
            Date dateParsed = dateFormat.parse(date);
            return dateParsed;
        } catch (ParseException e) {
            System.out.println("Error parse date");
            e.printStackTrace();
        }

        return null;
    }

    private Status readStatus(String status) {
        switch (status) {
            case "ERROR":
                return Status.ERROR;
            case "FAILED":
                return Status.FAILED;
            case "OK":
                return Status.OK;
            default:
                return null;
        }
    }

    private boolean dateBetweenDates(Date current, Date after, Date before) {
        if (after == null) {
            after = new Date(0);
        }
        if (before == null) {
            before = new Date(Long.MAX_VALUE);
        }
        return current.after(after) && current.before(before);
    }

    @Override
    public Set<String> getAllUsers() {
        Set<String> result = new HashSet<>();

        for (LogFile log : logLines) {
            result.add(log.getUserName());
        }
        return result;

    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                result.add(log.getUserName());
            }
        }
        return result.size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (user.equals(log.getUserName())) {
                    result.add(log.getEvent().name());
                }
            }
        }
        return result.size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (ip.equals(log.getIp())) {
                    result.add(log.getUserName());
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.LOGIN)) {
                    result.add(log.getUserName());
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.DOWNLOAD_PLUGIN)) {
                    result.add(log.getUserName());
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.WRITE_MESSAGE)) {
                    result.add(log.getUserName());
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.SOLVE_TASK)) {
                    result.add(log.getUserName());
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.SOLVE_TASK) && log.getNumberTask() == task) {
                    result.add(log.getUserName());
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.DONE_TASK)) {
                    result.add(log.getUserName());
                }
            }
        }
        return result;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        Set<String> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.DONE_TASK) && log.getNumberTask() == task) {
                    result.add(log.getUserName());
                }
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getUserName().equals(user) && log.getEvent().equals(event)) {
                    result.add(log.getDate());
                }
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getStatus().equals(Status.FAILED)) {
                    result.add(log.getDate());
                }
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getStatus().equals(Status.ERROR)) {
                    result.add(log.getDate());
                }
            }
        }
        return result;
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getUserName().equals(user) && log.getEvent().equals(Event.LOGIN)) {
                    result.add(log.getDate());
                }
            }
        }
        if (result.size() == 0) return null;
        Date minDate = result.iterator().next();
        for (Date date : result) {
            if (date.getTime() < minDate.getTime()) {
                minDate = date;
            }
        }
        return minDate;
    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getUserName().equals(user)
                        && log.getEvent().equals(Event.SOLVE_TASK)
                        && log.getNumberTask() == task) {
                    result.add(log.getDate());
                }
            }
        }
        if (result.size() == 0) return null;
        Date minDate = result.iterator().next();
        for (Date date : result) {
            if (date.getTime() < minDate.getTime()) {
                minDate = date;
            }
        }
        return minDate;
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getUserName().equals(user) && log.getEvent().equals(Event.DONE_TASK) &&
                        log.getNumberTask() == task) {
                    result.add(log.getDate());
                }
            }
        }
        if (result.size() == 0) return null;
        Date minDate = result.iterator().next();
        for (Date date : result) {
            if (date.getTime() < minDate.getTime()) {
                minDate = date;
            }
        }
        return minDate;
    }

    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getUserName().equals(user)
                        && log.getEvent().equals(Event.WRITE_MESSAGE)) {
                    result.add(log.getDate());
                }
            }
        }
        return result;
    }

    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        Set<Date> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getUserName().equals(user) && log.getEvent().equals(Event.DOWNLOAD_PLUGIN)) {
                    result.add(log.getDate());
                }
            }
        }
        return result;
    }

    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                result.add(log.getEvent());
            }
        }
        return result.size();
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                result.add(log.getEvent());
            }
        }
        return result;
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getIp().equals(ip)) {
                    result.add(log.getEvent());
                }
            }
        }
        return result;
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getUserName().equals(user)) {
                    result.add(log.getEvent());
                }
            }
        }
        return result;
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getStatus().equals(Status.FAILED)) {
                    result.add(log.getEvent());
                }
            }
        }
        return result;
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        Set<Event> result = new HashSet<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getStatus().equals(Status.ERROR)) {
                    result.add(log.getEvent());
                }
            }
        }
        return result;
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {

        int count = 0;
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.SOLVE_TASK) && log.getNumberTask() == task) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        int count = 0;
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.DONE_TASK) && log.getNumberTask() == task) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> result = new HashMap<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.SOLVE_TASK)) {
                    int task = log.getNumberTask();
                    Integer count = result.getOrDefault(task, 0);
                    result.put(task, count + 1);
                }
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> result = new HashMap<>();
        for (LogFile log : logLines) {
            if (dateBetweenDates(log.getDate(), after, before)) {
                if (log.getEvent().equals(Event.DONE_TASK)) {
                    int task = log.getNumberTask();
                    Integer count = result.getOrDefault(task, 0);
                    result.put(task, count + 1);
                }
            }
        }
        return result;
    }

    @Override
    public Set<Object> execute(String query) {
        Set<Object> result = new HashSet<>();
        DateFormat simpleDateFormat = new SimpleDateFormat("d.M.yyyy H:m:s");
        String field1;
        String field2 = null;
        String value1 = null;
        Date after = null;
        Date before = null;
        Pattern pattern = Pattern.compile("get (ip|user|date|event|status)"
                + "( for (ip|user|date|event|status) = \"(.*?)\")?"
                + "( and date between \"(.*?)\" and \"(.*?)\")?");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        field1 = matcher.group(1);
        if (matcher.group(2) != null) {
            field2 = matcher.group(3);
            value1 = matcher.group(4);
            if (matcher.group(5) != null) {
                try {
                    after = simpleDateFormat.parse(matcher.group(6));
                    before = simpleDateFormat.parse(matcher.group(7));
                } catch (ParseException e) {
                }
            }
        }
        if (field2 != null && value1 != null) {
            for (int i = 0; i < logLines.size(); i++) {
                if (dateBetweenDates(logLines.get(i).getDate(), after, before)) {
                    if (field2.equals("date")) {
                        try {
                            if (logLines.get(i).getDate().getTime() == simpleDateFormat.parse(value1).getTime()) {
                                result.add(getCurrentValue(logLines.get(i), field1));
                            }
                        } catch (ParseException e) {
                        }
                    } else {
                        if (value1.equals(getCurrentValue(logLines.get(i), field2).toString())) {
                            result.add(getCurrentValue(logLines.get(i), field1));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < logLines.size(); i++) {
                result.add(getCurrentValue(logLines.get(i), field1));
            }
        }

        return result;
    }

    private Object getCurrentValue(LogFile logFile, String field) {
        Object value = null;
        switch (field) {
            case "ip": {
                Command method = new GetIpCommand(logFile);
                value = method.execute();
                break;
            }
            case "user": {
                Command method = new GetUserCommand(logFile);
                value = method.execute();
                break;
            }
            case "date": {
                Command method = new GetDateCommand(logFile);
                value = method.execute();
                break;
            }
            case "event": {
                Command method = new GetEventCommand(logFile);
                value = method.execute();
                break;
            }
            case "status": {
                Command method = new GetStatusCommand(logFile);
                value = method.execute();
                break;
            }
        }
        return value;
    }

    private abstract class Command {
        protected LogFile logFile;

        abstract Object execute();
    }

    private class GetIpCommand extends Command {
        public GetIpCommand(LogFile logFile) {
            this.logFile = logFile;
        }

        @Override
        Object execute() {
            return logFile.getIp();
        }
    }

    private class GetUserCommand extends Command {
        public GetUserCommand(LogFile logFile) {
            this.logFile = logFile;
        }

        @Override
        Object execute() {
            return logFile.getUserName();
        }
    }

    private class GetDateCommand extends Command {
        public GetDateCommand(LogFile logFile) {
            this.logFile = logFile;
        }

        @Override
        Object execute() {
            return logFile.getDate();
        }
    }

    private class GetEventCommand extends Command {
        public GetEventCommand(LogFile logFile) {
            this.logFile = logFile;
        }

        @Override
        Object execute() {
            return logFile.getEvent();
        }
    }

    private class GetStatusCommand extends Command {
        public GetStatusCommand(LogFile logFile) {
            this.logFile = logFile;
        }

        @Override
        Object execute() {
            return logFile.getStatus();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class LogFile {
        private List<File> logFile = new ArrayList<>();
        private String ip;
        private String userName;
        private Date date;
        private Event event;
        private int numberTask;
        private Status status;

        public LogFile(String ip, String userName, Date date, Event event, Status status) {
            this.ip = ip;
            this.userName = userName;
            this.date = date;
            this.event = event;
            this.status = status;
        }

        public LogFile(String ip, String userName, Date date, Event event, int numberTask, Status status) {
            this.ip = ip;
            this.userName = userName;
            this.date = date;
            this.event = event;
            this.numberTask = numberTask;
            this.status = status;
        }

        public LogFile() {
        }

        @Override
        public String toString() {
            return "LogFile{" +
                    "ip='" + ip + '\'' +
                    ", userName='" + userName + '\'' +
                    ", date=" + date +
                    ", event=" + event +
                    ", numberTask=" + numberTask +
                    ", status=" + status +
                    '}';
        }

        public String getIp() {
            return ip;
        }

        public String getUserName() {
            return userName;
        }

        public Date getDate() {
            return date;
        }

        public Event getEvent() {
            return event;
        }

        public Status getStatus() {
            return status;
        }

        public int getNumberTask() {
            return numberTask;
        }
    }
}