package session;

import exception.EditorException;
import image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionManager {
    private final Map<Integer, Session> sessions;
    private int nextSessionId;
    private Session activeSession;

    public SessionManager() {
        this.sessions = new HashMap<>();
        this.nextSessionId = 1;
        this.activeSession = null;
    }

    /**
     * Връща валидна текуща сесия.
     * @return Текущата активна сесия.
     * @throws EditorException Ако няма активна сесия.
     */
    public Session getValidatedActiveSession() throws EditorException {
        if (activeSession == null) {
            throw new EditorException("No active session. Use 'load <file...>' to start a new session or switch to an existing one.");
        }
        return activeSession;
    }

    public int getSessionCount() {
        return this.sessions.size();
    }

    public Map<Integer, Session> getSessions() {
        return new HashMap<>(sessions);
    }

    public Session getActiveSession() {
        return activeSession;
    }

    public Integer getActiveSessionId() {
        return activeSession != null ? activeSession.getId() : null;
    }

    /**
     * Създава нова сесия с подадените изображения.
     * @param images Колекция с изображения за новата сесия.
     */
    public void createSession(List<Image> images) {
        Session newSession = new Session(nextSessionId, images);
        sessions.put(nextSessionId, newSession);
        activeSession = newSession;
        nextSessionId++;
    }

    /**
     * Създава празна сесия.
     */
    public void createSession() {
        Session newSession = new Session(nextSessionId);
        sessions.put(nextSessionId, newSession);
        activeSession = newSession;
        nextSessionId++;
    }

    /**
     * Превключва към сесия с дадено ID.
     * @param sessionId ID на сесията.
     * @throws EditorException Ако сесията не съществува.
     */
    public void switchSession(int sessionId) throws EditorException {
        if (!sessions.containsKey(sessionId)) {
            throw new EditorException("Session with ID " + sessionId + " does not exist.");
        }
        activeSession = sessions.get(sessionId);
    }

    /**
     * Премахва текущата активна сесия от колекцията със сесии.
     */
    public void closeCurrentSession() {
        if (activeSession != null) {
            int closedSessionId = activeSession.getId();
            sessions.remove(closedSessionId);

            System.out.println("Session " + closedSessionId + " closed successfully");  // ← ПЪРВО

            if (!sessions.isEmpty()) {
                Integer firstSessionId = sessions.keySet().iterator().next();
                activeSession = sessions.get(firstSessionId);
                System.out.println("Auto-switched to session ID: " + firstSessionId);  // ← ПОСЛЕ
            } else {
                activeSession = null;
            }
        }
    }

    /**
     * Добавя изображение към активната сесия.
     * @param image Изображението за добавяне.
     * @throws EditorException Ако няма активна сесия.
     */
    public void addImageToActiveSession(Image image) throws EditorException {
        getValidatedActiveSession().addImage(image);
    }

    /**
     * Проверява дали сесия съществува.
     * @param sessionId ID на сесията.
     * @return true ако сесията съществува, false иначе.
     */
    public boolean sessionExists(int sessionId) {
        return sessions.containsKey(sessionId);
    }
}
